package in.tech_camp.chat_app.factories;

import org.springframework.mock.web.MockMultipartFile;

import com.github.javafaker.Faker;

import in.tech_camp.chat_app.form.MessageForm;

public class MessageFormFactory {
  private static final Faker faker=new Faker();

  public static MessageForm createMessageForm(){
    MessageForm messageForm =new MessageForm();
    
    messageForm.setContent(faker.lorem().sentence());
    messageForm.setImage(new MockMultipartFile("image","image.jpg","image/jpeg",faker.avatar().image().getBytes()));
    
//  faker.avatar()：アバター作成ツールを呼び出す。
// .image()：具体的な「画像データ」を生成する。
// .getBytes()：その画像を、保存や転送ができる 「バイトの塊（byte配列）」 に変換する。

    return messageForm;
  }

  
  
}
