package com.matmic.cookbook.controller.util;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PaginationUtilUnitTest {

    @Test
    public void generatePaginationHttpHeadersTest() throws Exception{
        String baserUrl = "/api/comments/test";
        List<String> content = new ArrayList<>();
        Page<String> page = new PageImpl<>(content, PageRequest.of(2, 50), 500L);
        HttpHeaders headers = PaginationUtil.paginationHttpHeader(page, baserUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);

        assertNotNull(strHeaders);
        assertTrue(strHeaders.size() == 1);

        String headerData = strHeaders.get(0);

        assertTrue(headerData.split(",").length == 4);
        String expectedData = "</api/comments/test?page=3&size=50>; rel=\"next\"," +
                "</api/comments/test?page=1&size=50>; rel=\"prev\"," +
                "</api/comments/test?page=9&size=50>; rel=\"last\"," +
                "</api/comments/test?page=0&size=50>; rel=\"first\"";

        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertTrue(xTotalCountHeaders.size() == 1);
        assertTrue(Long.valueOf(xTotalCountHeaders.get(0)).equals(500L));

    }
}
