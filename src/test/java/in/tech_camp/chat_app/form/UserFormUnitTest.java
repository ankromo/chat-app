package in.tech_camp.chat_app.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.factories.UserFormFactory;
import in.tech_camp.chat_app.validation.ValidationPriority1;
import in.tech_camp.chat_app.validation.ValidationPriority2;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
@SpringBootTest
public class UserFormUnitTest {
  private UserForm userForm;

  private Validator validator;

  private BindingResult bindingResult;

  @BeforeEach
  public void setUp(){
    userForm=UserFormFactory.createUser();

    ValidatorFactory factory=Validation.buildDefaultValidatorFactory();
    validator=factory.getValidator();

    bindingResult=Mockito.mock(BindingResult.class);

  }

  @Nested
  class ユーザーを作成できる場合{
    @Test
    public void nameとemailとpasswordconfirmationが存在すれば登録できる(){
       Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority1.class);
      assertEquals(0,violations.size());
      //バリデーションの結果として発生したエラー情報がSet<ConstraintViolation<UserForm>> violationsに保存されるから、このエラー情報がassertEqualsで0ならエラーがないということになる
    }
  }

  @Nested
  class ユーザーを作成できない場合{
    @Test
    public void nameが空では登録できない(){
      userForm.setName("");
      Set<ConstraintViolation<UserForm>> violations=validator.validate(userForm,ValidationPriority1.class);
      assertEquals(1, violations.size());
      assertEquals("Name can't be blank",violations.iterator().next().getMessage());
    }

    @Test
    public void emailが空では登録できない(){
      userForm.setEmail("");
      Set<ConstraintViolation<UserForm>>violations=validator.validate(userForm,ValidationPriority1.class);
      assertEquals(1,violations.size());
      assertEquals("Email can't be blank",violations.iterator().next().getMessage());

    }

    @Test
    public void passwordが空では登録できない(){
      userForm.setPassword("");
      Set<ConstraintViolation<UserForm>>violations=validator.validate(userForm,ValidationPriority1.class);
      assertEquals(1,violations.size());
      assertEquals("Password can't be blank",violations.iterator().next().getMessage());
    }
    @Test
    public void emailはアットマークを含まないと登録できない(){
      userForm.setEmail("atmarknai");
      Set<ConstraintViolation<UserForm>>violations=validator.validate(userForm,ValidationPriority2.class);
      assertEquals(1,violations.size());
      assertEquals("Email should be valid",violations.iterator().next().getMessage());
    // violations: 「エラー通知書が何枚か入った封筒」がある。
// .iterator().next(): 封筒から「1枚目の通知書」を手に取る。
// .getMessage(): その通知書に書いてある「不備の内容（文字）」を読み取る。
// assertEquals(...): その内容が「Email should be valid」という文字と完全に一致するか見比べる。
  }
  @Test
  public void passwordが5文字以下では登録できない(){
      userForm.setPassword("abcde");
      Set<ConstraintViolation<UserForm>>violations=validator.validate(userForm,ValidationPriority2.class);

  }

  @Test
  public void passwordが129文字以上では登録できない(){
    String password="a".repeat(129);
    userForm.setPassword(password);
    Set<ConstraintViolation<UserForm>>violations=validator.validate(userForm,ValidationPriority2.class);
    assertEquals(1,violations.size());
    assertEquals("Password should be between 6 and 128 characters",violations.iterator().next().getMessage());

  }

  @Test
  public void passwordとpasswordConfirmationが不一致では登録できない(){
    userForm.setPasswordConfirmation("differentPassword");
    //（）にわざと違うパスワードを入力する
    userForm.validatePasswordConfirmation(bindingResult);
    //想定どおり不合格か判定させる　userFormにわざと違うパスワードが入ってる　bindingResultというエラーメッセージ書き込む変数を渡す
    verify(bindingResult).rejectValue("passwordConfirmation","error.user","Password confirmation doesn't match Password");
    //bindingResultにメソッドrejectValue（エラーが起きた場所・項目名,エラーの種類,エラー文)を引数として渡して一致するか確認

  }

}
}