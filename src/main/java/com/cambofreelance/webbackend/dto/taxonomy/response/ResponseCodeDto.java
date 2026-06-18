package com.cambofreelance.webbackend.dto.taxonomy.response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseCodeDto implements Serializable {

    private Long id;
    private String code;
    private String httpStatus;
    private String key;
    private String type;
    private String description;
    private String message;
    private String messageKh;
    private String messageCn;
    private String status;

//  public void setData(ResponseCodeEntity data) {
//    this.id = data.getId();
//    this.code = data.getCode();
//    this.httpStatus = data.getHttpStatus();
//    this.key = data.getKey();
//    this.type = data.getType();
//    this.description = data.getDescription();
//    this.message = data.getMessage();
//    this.messageKh = data.getMessageKh();
//    this.messageCn = data.getMessageCn();
//    this.status = data.getStatus();
//  }

    public static Map<String, String> getErrorMessage(ResponseCodeDto responseCode) {
        Map<String, String> message = new HashMap<>();
        message.put("message", StringUtils.defaultIfEmpty(responseCode.message, ""));
        message.put("message_kh", StringUtils.defaultIfEmpty(responseCode.messageKh, message.get(message)));
        message.put("message_cn", StringUtils.defaultIfEmpty(responseCode.messageCn, message.get(message)));

        return message;
    }
}

