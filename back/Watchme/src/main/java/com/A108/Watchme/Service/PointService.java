package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveRes;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayRes;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Service
public class PointService {
    private MemberRepository memberRepository;

    public KakaoPayRes kakaoPayReady(Long id, Integer point) {
        System.out.println("hello");
        KakaoPayReq kakaoPayReq = KakaoPayReq.builder()
                .cid("TC0ONETIME")
                .partner_order_id("포인트 결제")
                .partner_user_id(Long.toString(id))
                .item_name("WATCH ME POINT 결제")
                .quantity(1)
                .total_amount(point)
                .tax_free_amount(0)
                .approval_url("http://localhost:81")
                .fail_url("http://localhost:81")
                .cancel_url("http://localhost:81")
                .build();

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK f40f549f7b42eada530093aefb9689ab");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", kakaoPayReq.getCid());
        params.add("partner_order_id", kakaoPayReq.getPartner_order_id());
        params.add("partner_user_id", kakaoPayReq.getPartner_user_id());
        params.add("item_name", kakaoPayReq.getItem_name());
        params.add("quantity", Integer.toString(kakaoPayReq.getQuantity()));
        params.add("total_amount", Integer.toString(kakaoPayReq.getTotal_amount()));
        params.add("tax_free_amount", Integer.toString(kakaoPayReq.getTax_free_amount()));
        params.add("approval_url", kakaoPayReq.getApproval_url());
        params.add("cancel_url", kakaoPayReq.getCancel_url());
        params.add("fail_url", kakaoPayReq.getFail_url());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/payment/ready",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        KakaoPayRes kakaoPayRes = new KakaoPayRes();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            kakaoPayRes = objectMapper.readValue(response.getBody(), KakaoPayRes.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoPayRes;
    }
    public KakaoPayApproveRes kakaoPayApprove(Long id, String pg_token, KakaoPayApproveReq kakaoPayApproveReq){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK f40f549f7b42eada530093aefb9689ab");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayApproveReq.getTid());
        params.add("partner_order_id", "포인트 결제");
        params.add("partner_user_id", Long.toString(id));
        params.add("pg_token", pg_token);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/payment/approve",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        KakaoPayApproveRes kakaoPayApproveRes = new KakaoPayApproveRes();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            kakaoPayApproveRes = objectMapper.readValue(response.getBody(), KakaoPayApproveRes.class);
        } catch (JsonMappingException exception) {
            exception.printStackTrace();
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }
        return kakaoPayApproveRes;
    }
}
