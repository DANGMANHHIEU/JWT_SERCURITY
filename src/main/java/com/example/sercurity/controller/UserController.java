package com.example.sercurity.controller;

import com.example.sercurity.jwt.JwtTokenProvider;
import com.example.sercurity.model.entity.ERole;
import com.example.sercurity.model.entity.Roles;
import com.example.sercurity.model.entity.Users;
import com.example.sercurity.model.service.RoleService;
import com.example.sercurity.model.service.UserService;
import com.example.sercurity.payload.req.LoginReq;
import com.example.sercurity.payload.req.SignupReq;
import com.example.sercurity.payload.resp.JwtResp;
import com.example.sercurity.payload.resp.MessageResp;
import com.example.sercurity.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager; // giúp xác thực
    @Autowired
    private JwtTokenProvider tokenProvider;  //lâ userName từ token

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder; // để mã hóa password

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupReq signupReq){
        if(userService.existsByUserName(signupReq.getUserName())){
            return ResponseEntity.badRequest().body(new MessageResp("Error: UserName is already !!!"));
        }
        if(userService.existsByEmail(signupReq.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResp("Error: Email is already !!!"));
        }
        Users users = new Users();
        users.setUserName(signupReq.getUserName());
        users.setPassword(passwordEncoder.encode(signupReq.getPassword()));
        users.setEmail(signupReq.getEmail());
        users.setPhone(signupReq.getPhone());
        users.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try{
            users.setCreated(sdf.parse(strNow));
        }catch (Exception e){
            e.printStackTrace();
        }
        Set<String> strRoles= signupReq.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if(strRoles ==null){ // nếu truyền sang không truyển roles nào mặc định là User
            Roles userRoles = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            listRoles.add(userRoles);
        }
        else {
            strRoles.forEach(role ->{
                switch (role){
                    case "admin":
                        Roles adminRoles = roleService.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(adminRoles);
                        break;
                    case"user":
                        Roles userRoles = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(userRoles);
                        break;
                    case"moderator":
                        Roles moderatorRoles = roleService.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        listRoles.add(moderatorRoles);
                        break;
                }
            });
        }
        users.setListRoles(listRoles);
        userService.saveOrUpdate(users);
        return ResponseEntity.ok(new MessageResp("User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginReq loginReq){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUserName(),loginReq.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // sinh ra JWT trả về cho client
        String jwt = tokenProvider.generateToken(customUserDetails);
        List<String> listRoles = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        return  ResponseEntity.ok(new JwtResp(jwt,customUserDetails.getUsername(),customUserDetails.getEmail(),customUserDetails.getPhone(),listRoles));
    }

//    @GetMapping("/list")
//    public ResponseEntity<?> list(){
//        Users users = (Users) userService.findALL();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }


}
