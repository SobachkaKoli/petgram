package com.example.petgram.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
//  TODO (Bogdan O.) 26/4/23: check all your dtos if they're really need constructors
@AllArgsConstructor
public class PostDTO {

    private String text;
    private List<MultipartFile> pictures;

}
