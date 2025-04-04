package com.TrungTinhBackend.codearena_backend.Service.Blog;

import com.TrungTinhBackend.codearena_backend.Entity.Blog;
import com.TrungTinhBackend.codearena_backend.Entity.Course;
import com.TrungTinhBackend.codearena_backend.Entity.User;
import com.TrungTinhBackend.codearena_backend.Exception.NotFoundException;
import com.TrungTinhBackend.codearena_backend.Repository.BlogRepository;
import com.TrungTinhBackend.codearena_backend.Request.APIRequestBlog;
import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Img.ImgService;
import com.TrungTinhBackend.codearena_backend.Service.Search.Specification.BlogSpecification;
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
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ImgService imgService;

    @Autowired
    private VideoService videoService;

    public BlogServiceImpl(BlogRepository blogRepository, ImgService imgService, VideoService videoService) {
        this.blogRepository = blogRepository;
        this.imgService = imgService;
        this.videoService = videoService;
    }

    @Override
    public APIResponse addBlog(APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception {
       APIResponse apiResponse = new APIResponse();
            Blog blog = new Blog();

            blog.setBlogName(apiRequestBlog.getBlogName());
            blog.setDescription(apiRequestBlog.getDescription());
            if(img != null && !img.isEmpty()) {
                blog.setImg(imgService.uploadImg(img));
            }
            if(video != null && !video.isEmpty()) {
                blog.setVideo(videoService.uploadVideo(video));
            }
            blog.setDate(LocalDateTime.now());
            blog.setDeleted(false);
            blog.setLikeCount(0L);
            blog.setView(0L);

            blogRepository.save(blog);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Add blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;

    }

    @Override
    public APIResponse updateBlog(Long id, APIRequestBlog apiRequestBlog, MultipartFile img, MultipartFile video) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Blog blog = blogRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Blog not found by id " + id)
            );

            blog.setBlogName(apiRequestBlog.getBlogName());
            blog.setDescription(apiRequestBlog.getDescription());
            if(img != null && !img.isEmpty()) {
                blog.setImg(imgService.updateImg(blog.getImg(),img));
            }
            if(video != null && !video.isEmpty()) {
                blog.setVideo(videoService.updateVideo(blog.getVideo(), video));
            }

            blogRepository.save(blog);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Update blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse deleteBlog(Long id) throws Exception {
        APIResponse apiResponse = new APIResponse();

            Blog blog = blogRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Blog not found by id " + id)
            );

            blog.setDeleted(true);

            blogRepository.save(blog);

            apiResponse.setStatusCode(200L);
            apiResponse.setMessage("Delete blog success !");
            apiResponse.setData(blog);
            apiResponse.setTimestamp(LocalDateTime.now());

            return apiResponse;
    }

    @Override
    public APIResponse searchBlog(String keyword, int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Specification<Blog> specification = BlogSpecification.searchByKeyword(keyword);
        Page<Blog> blogs = blogRepository.findAll(specification,pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Search blog success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogByPage(int page, int size) {
        APIResponse apiResponse = new APIResponse();

        Pageable pageable = PageRequest.of(page,size);
        Page<Blog> blogs = blogRepository.findAll(pageable);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by page success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getAllBlog() {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findAll();

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get all blog success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogById(Long id) {
        APIResponse apiResponse = new APIResponse();

        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Blog not found !")
        );

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by id success !");
        apiResponse.setData(blog);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

    @Override
    public APIResponse getBlogByUserId(Long userId) {
        APIResponse apiResponse = new APIResponse();

        List<Blog> blogs = blogRepository.findByUserId(userId);

        apiResponse.setStatusCode(200L);
        apiResponse.setMessage("Get blog by userId success !");
        apiResponse.setData(blogs);
        apiResponse.setTimestamp(LocalDateTime.now());

        return apiResponse;
    }

}
