package com.TrungTinhBackend.codearena_backend.Service.BlogComment;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.BlogComment;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.BlogCommentRepository;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Repository.UserRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlogComment;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.BlogCommentSpecification;
import com.TrungTinhBackend.codearena_backend.Service.Video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService{

    @Autowired
    private BlogCommentRepository blogCommentRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    public BlogCommentServiceImpl(BlogCommentRepository blogCommentRepository, ImgService imgService, VideoService videoService, BlogRepository blogRepository, UserRepository userRepository) {
        this.blogCommentRepository = blogCommentRepository;
        this.imgService = imgService;
        this.videoService = videoService;
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    @Override
    public APIResponse addBlogComment(APIRequestBlogComment apiRequestBlogComment, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            User user = userRepository.findById(apiRequestBlogComment.getUser().getId()).orElseThrow(
                    () -> new NotFoundException("User not found by id " + apiRequestBlogComment.getUser().getId())
            );

            Blog blog = blogRepository.findById(apiRequestBlogComment.getBlog().getId()).orElseThrow(
                    () -> new NotFoundException("Blog not found by id " + apiRequestBlogComment.getBlog().getId())
            );

            BlogComment blogComment = new BlogComment();

            blogComment.setContent(apiRequestBlogComment.getContent());
            if(img != null && !img.isEmpty()) {
                blogComment.setImg(imgService.uploadImg(img));
            }
            if(video != null && !video.isEmpty()) {
                blogComment.setVideo(videoService.uploadVideo(video));
            }
            blogComment.setUser(user);
            blogComment.setBlog(blog);
            blogComment.setDate(LocalDateTime.now());
            blogComment.setDeleted(false);

            blogCommentRepository.save(blogComment);


            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add blog comment success !");
            apiResponse.setData(blogComment);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse deleteBlogComment(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            BlogComment blogComment = blogCommentRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Blog comment not found by id " + id)
            );

            blogComment.setDeleted(true);

            blogCommentRepository.save(blogComment);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete blog comment success !");
            apiResponse.setData(blogComment);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchBlogComment(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<BlogComment> specification = BlogCommentSpecification.searchByKeyword(keyword);
        Page<BlogComment> blogComments = blogCommentRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search blog comment success !");
        apiResponse.setData(blogComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogCommentByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<BlogComment> blogComments = blogCommentRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog comment by page success !");
        apiResponse.setData(blogComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllBlogComment() {
        APIResponse apiResponse = new APIResponse();

        List<BlogComment> blogComments = blogCommentRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all blog comment success !");
        apiResponse.setData(blogComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogCommentById(Long id) {
        APIResponse apiResponse = new APIResponse();

        BlogComment blogComment = blogCommentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Blog comment not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog comment by id success !");
        apiResponse.setData(blogComment);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogCommentByBlogId(Long blogId) {
        APIResponse apiResponse = new APIResponse();

        List<BlogComment> blogComments = blogCommentRepository.findByBlogId(blogId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog comment by blog id success !");
        apiResponse.setData(blogComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogCommentByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<BlogComment> blogComments = blogCommentRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog comment by user id success !");
        apiResponse.setData(blogComments);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }
}
