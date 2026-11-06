package com.example.projectIntern.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private int currentPage;   // Trang hiện tại (Frontend thường đếm từ 1)
    private int totalPages;    // Tổng số trang
    private int pageSize;      // Số bản ghi trên 1 trang
    private long totalElements; // Tổng số bản ghi trong DB
    private List<T> data;      // Mảng dữ liệu thực tế
}