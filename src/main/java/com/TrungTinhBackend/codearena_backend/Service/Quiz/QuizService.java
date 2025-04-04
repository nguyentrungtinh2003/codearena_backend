package com.TrungTinhBackend.codearena_backend.Service.Quiz;

import com.TrungTinhBackend.codearena_backend.Request.APIRequestQuiz;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import org.springframework.web.multipart.MultipartFile;

public interface QuizService {
    public APIResponse addQuiz(APIRequestQuiz apiRequestQuiz, MultipartFile img) throws Exception;
    public APIResponse updateQuiz(Long id, APIRequestQuiz apiRequestQuiz, MultipartFile img) throws Exception;
    public APIResponse deleteQuiz(Long id) throws Exception;
    public APIResponse searchQuiz(String keyword, int page, int size);
    public APIResponse getQuizByPage(int page, int size);
    public APIResponse getAllQuiz();
    public APIResponse getQuizById(Long id);
}
