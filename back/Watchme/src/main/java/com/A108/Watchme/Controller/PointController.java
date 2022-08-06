package com.A108.Watchme.Controller;


import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayRes;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Service.PointService;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PointController {
    @Autowired
    private PointService pointService;
    @Autowired
    private MemberRepository memberRepository;
    @PostMapping("/points/kakao")
    public ApiResponse kakaoPayReady(@RequestParam(name = "value") String value){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(((UserDetails)authentication.getPrincipal()).getUsername());
        System.out.println("----------------------------");
        Long id = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());

        int point = Integer.parseInt(value);
        KakaoPayRes kakaoPayRes = pointService.kakaoPayReady(id, point);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("PAY READY SUCCESS");
        apiResponse.setResponseData("REDIRECT_URL", kakaoPayRes.getNext_redirect_pc_url());
        apiResponse.setResponseData("tid", kakaoPayRes.getTid());
        return apiResponse;
    }

    @PostMapping("/points/kakao/approval")
    public ApiResponse kakaoApprove(@RequestParam(required = false, name="pg_token") String pg_token, @RequestBody KakaoPayApproveReq kakaoPayApproveReq){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());
        pointService.kakaoPayApprove(id, pg_token, kakaoPayApproveReq);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("PAY READY SUCCESS");
        return apiResponse;
    }
}


