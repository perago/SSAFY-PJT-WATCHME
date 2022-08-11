package com.A108.Watchme.Controller;


import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayRes;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
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
        Long id;
        try{
            id = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());
        }
        catch(Exception e){
            throw new CustomException(Code.C501);
        }
        int point = 0;
        try{
            point = Integer.parseInt(value);
            if(point <= 0){
                throw new CustomException(Code.C598);
            }
        } catch (Exception e){
            throw new CustomException(Code.C521);
        }

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

    @PostMapping("/points/return")
    public ApiResponse apiResponse(@RequestParam(required = false, value="value") int value) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

        return null;
    }

}


