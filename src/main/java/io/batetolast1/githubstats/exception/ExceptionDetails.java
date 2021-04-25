package io.batetolast1.githubstats.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionDetails {

    protected String title;
    protected int status;
    protected String details;
    protected LocalDateTime timestamp;
    protected String developerMessage;
}
