package cambo.freelance.webservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class MessageDataResponse {
    private String code;
    private String message;
    private String messageKH;
    private Object data;
    private Map<String, Object> metadata; // Add this field

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public void setMetadata(Page<?> page) {
        this.metadata = Map.of(
                "pageNumber", page.getNumber() + 1, // 1-based page number
                "pageSize", page.getSize(),
                "totalRecords", page.getTotalElements(),
                "totalPages", page.getTotalPages(),
                "first", page.isFirst(),
                "last", page.isLast(),
                "numberOfElements", page.getNumberOfElements(),
                "sort", page.getSort().toString(),
                "empty", page.isEmpty()
        );
    }


    public void getDataSuccess(Object data){
        this.setCode("200");
        this.setMessage("Data Successfully Retrieve");
        this.setMessageKH("ទិន្នន័យទទួលបានជោគជ័យ");
        this.setData(data);
    }

    public void getUserNotFound() {
        this.code = "400";
        this.message = "The user is not found!";
        this.messageKH = "រកមិនឃើញអ្នកប្រើប្រាស់ទេ!";
    }

    public void setNameAlreadyUse() {
        this.code = "400";
        this.message = "The name is already use";
        this.messageKH = "ឈ្មោះនេះប្រើរួចហើយ";
    }
    public void setMessageCreateSuccess(Object data) {
        this.code = "200";
        this.message = "Create Success";
        this.messageKH = "បង្កើតជោគជ័យ";
        this.data = data;
    }

    public void setEmailAlreadyUse() {
        this.code = "400";
        this.message = "The email is already used";
        this.messageKH = "អ៊ីមែលត្រូវបានប្រើប្រាស់រួចហើយ";
    }
    public void getDataError(Object data){
        this.setCode("400");
        this.setMessage("Data Cannot Be Found");
        this.setMessageKH("មិនមានទិន្នន័យ");
        this.setData(data);
    }

    public void createDataSuccess(Object data){
        this.setCode("200");
        this.setMessage("Data Successfully Created");
        this.setMessageKH("ការបង្កើតទទួលបានជោគជ័យ");
        this.setData(data);
    }

    public void createDataError(Object data, String message) {
        this.setCode("400");
        this.setMessage(message != null && !message.isEmpty() ? message : "Data Cannot Be Created");
        this.setMessageKH("ការបង្កើតមិនទទួលបានជោគជ័យ");
        this.setData(data);
    }


    public void updateDataSuccess(Object data){
        this.setCode("200");
        this.setMessage("Data Successfully Updated");
        this.setMessageKH("ការកែប្រែទទួលបានជោគជ័យ");
        this.setData(data);
    }

    public void deleteDataSuccess(Object data){
        this.setCode("200");
        this.setMessage("Data Successfully Deleted");
        this.setMessageKH("ការលុបទទួលបានជោគជ័យ");
        this.setData(data);
    }

    public void generalDataError(Object data){
        this.setCode("500");
        this.setMessage("Internal Server Error");
        this.setMessageKH("កំហុសម៉ាស៊ីនមេខាងក្នុង");
        this.setData(data);
    }

    public void internalServerError(Object data) {
        this.code = "500";
        this.message = "Internal Server Error";
        this.messageKH = "កំហុសម៉ាស៊ីនមេខាងក្នុង";
        this.data = data;
    }
}
