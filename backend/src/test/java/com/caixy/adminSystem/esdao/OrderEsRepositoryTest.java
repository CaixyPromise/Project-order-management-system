package com.caixy.adminSystem.esdao;

import com.caixy.adminSystem.model.dto.order.OrderInfoEsDTO;
import com.caixy.adminSystem.model.dto.post.PostQueryRequest;
import com.caixy.adminSystem.model.entity.Post;
import com.caixy.adminSystem.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 帖子 ES 操作测试
 */
@SpringBootTest
public class OrderEsRepositoryTest
{

    @Resource
    private OrderEsRepository orderEsRepository;

    @Resource
    private PostService postService;

    @Test
    void test()
    {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Post> page =
                postService.searchFromEs(postQueryRequest);
        System.out.println(page);
    }

    @Test
    void testSelect()
    {
        System.out.println(orderEsRepository.count());
        Page<OrderInfoEsDTO> PostPage = orderEsRepository.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<OrderInfoEsDTO> postList = PostPage.getContent();
        System.out.println(postList);
    }

    @Test
    void testAdd()
    {
        OrderInfoEsDTO OrderInfoEsDTO = new OrderInfoEsDTO();

        orderEsRepository.save(OrderInfoEsDTO);
        System.out.println(OrderInfoEsDTO.getId());
    }

    @Test
    void testFindById()
    {
        Optional<OrderInfoEsDTO> OrderInfoEsDTO = orderEsRepository.findById(1L);
        System.out.println(OrderInfoEsDTO);
    }

    @Test
    void testCount()
    {
        System.out.println(orderEsRepository.count());
    }

}
