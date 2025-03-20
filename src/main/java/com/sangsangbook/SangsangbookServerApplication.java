package com.sangsangbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class SangsangbookServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SangsangbookServerApplication.class, args);
	}

}
 // 용원님 코드 
@RestController
@RequestMapping("/api")
class UserController {

    // 임시 사용자 목록
    private final List<User> users = new ArrayList<>();

    public UserController() {
        users.add(new User(1, "test", "1234"));
    }

    // 서버 상태 체크
    @GetMapping("/")
    public String hello() {
        return "서버가 정상적으로 실행 중.";
    }

    // API 상태 체크
    @GetMapping
    public String apiStatus() {
        return "API 엔드포인트가 정상적으로 실행 중.";
    }

    // 로그인 API
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            System.out.println("로그인 시도: " + email + ", " + password);  // 디버깅용 로그

            // 사용자 찾기
            User user = users.stream()
                    .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                System.out.println("로그인 실패: 사용자 정보가 일치하지 않습니다.");
                return new Response(false, "아이디 또는 비밀번호가 틀렸습니다.");
            }

            // 로그인 성공
            System.out.println("로그인 성공: " + user.getEmail());
            return new Response(true, "로그인에 성공하였습니다.", new UserResponse(user.getId(), user.getEmail()));

        } catch (Exception e) {
            System.err.println("로그인 오류: " + e.getMessage());
            return new Response(false, "서버 오류가 발생했습니다.");
        }
    }

    // 사용자 정보 조회 API
    @GetMapping("/user/{id}")
    public Response getUser(@PathVariable int id) {
        User user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);

        if (user == null) {
            return new Response(false, "사용자를 찾을 수 없습니다.");
        }

        return new Response(true, "사용자 정보", new UserResponse(user.getId(), user.getEmail()));
    }
}

class User {
    private int id;
    private String email;
    private String password;

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

class LoginRequest {
    private String email;
    private String password;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class UserResponse {
    private int id;
    private String email;

    public UserResponse(int id, String email) {
        this.id = id;
        this.email = email;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}

class Response {
    private boolean success;
    private String message;
    private UserResponse user;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Response(boolean success, String message, UserResponse user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserResponse getUser() {
        return user;
    }
}