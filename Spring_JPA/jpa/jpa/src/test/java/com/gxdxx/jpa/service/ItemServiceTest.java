package com.gxdxx.jpa.service;

import com.gxdxx.jpa.domain.item.Book;
import com.gxdxx.jpa.exception.NotEnoughStockException;
import com.gxdxx.jpa.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 상품등록() throws Exception {
        //given
        Book book = new Book();
        book.setName("book");

        //when
        itemService.saveItem(book);

        //then
        assertEquals(book, itemRepository.findOne(book.getId()));
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품_재고_예외() throws Exception {
        //given
        Book book = new Book();
        book.setName("book");
        book.setStockQuantity(100);

        //when
        book.removeStock(101);

        //then
        fail("예외가 발생해야 한다.");
    }
}