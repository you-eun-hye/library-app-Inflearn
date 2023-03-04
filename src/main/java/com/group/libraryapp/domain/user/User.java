package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @Column(nullable = false, length = 20)
    private String name;
    private Integer age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

    protected User(){}

    public User(String name, Integer age) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다.", name));
        }
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void updateName(String name){
        this.name = name;
    }

    public void loanBook(String bookName){
        this.userLoanHistories.add(new UserLoanHistory(this, bookName));
    }

    public void returnBook(String bookName){
        // 1. 함수형 프로그래밍을 할 수 있게, stream을 시작한다.
        UserLoanHistory targetHistory = this.userLoanHistories.stream()
                // 2. 들어오는 객체들 중 다음 조건을 충족하는 것만 필터링 한다.
                // 조건 : UserLoanHistory 중 책 이름이 bookName과 같은 것
                .filter(history -> history.getBookName().equals(bookName))
                // 3. 첫 번째로 해당하는 UserLoanHistory를 찾는다.
                .findFirst()
                // Optional을 제거하기 위해 없으면 예외를 던진다.
                .orElseThrow(IllegalArgumentException::new);
        // 4. 그렇게 찾은 UserLoanHistory를 반납처리 한다.
        targetHistory.doReturn();
    }

}





