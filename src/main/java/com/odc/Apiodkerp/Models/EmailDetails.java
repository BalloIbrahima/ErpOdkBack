package com.odc.Apiodkerp.Models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    // Annotations

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
// Class



}
