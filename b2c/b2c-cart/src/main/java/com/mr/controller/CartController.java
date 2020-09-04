package com.mr.controller;

import com.mr.bo.UserInfo;
import com.mr.common.utils.CookieUtils;
import com.mr.config.JwtConfig;
import com.mr.pojo.Cart;
import com.mr.service.CartService;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtConfig jwtConfig;

    @PostMapping("addCart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart,
                                        @CookieValue("B2C_TOKEN") String token){

        try {
            //获取用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());

            //添加购物车到缓存
            this.cartService.addCart(cart,userInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("queryCartList")
    public ResponseEntity<List<Cart>> queryCartList( @CookieValue("B2C_TOKEN") String token) {

        try {
            //获得登陆用户数据
            UserInfo userInfo= JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
            //根据用户查询购物车数据
            List<Cart> cartList = this.cartService.queryCartList(userInfo);
            //如果无购物车数据则返回无数据状态
            if(cartList==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(cartList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("update")
    public ResponseEntity<Void> updateNum(
            @RequestParam("skuId") Long skuId,
            @RequestParam("num") Integer num,
            @CookieValue("B2C_TOKEN") String token
    ){
   try {
            UserInfo userInfo= JwtUtils.getInfoFromToken(token,jwtConfig.getPublicKey());
        cartService.updateNum(skuId,num,userInfo);
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @DeleteMapping("deleteCart/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId,
                                           HttpServletRequest request){


        try {
            String toKen = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());
            //获取登录用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(toKen, jwtConfig.getPublicKey());
            this.cartService.deleteCart(skuId,userInfo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }




}
