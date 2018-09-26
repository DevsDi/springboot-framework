package com.dev.entity.system;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    @Getter
    @Setter
    @NonNull
    private int code;

    @Getter
    @Setter
    @NonNull
    private String message;

    @Getter
    @Setter
    private T data;

}
