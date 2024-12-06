package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.TaxInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxInfoService {
    
    /**
     * 获取税务信息列表（分页）
     */
    Page<TaxInfoDTO> getTaxInfos(Pageable pageable, Boolean active);

    /**
     * 根据ID获取税务信息
     */
    TaxInfoDTO getTaxInfo(Long id);

    /**
     * 创建税务信息
     */
    TaxInfoDTO createTaxInfo(TaxInfoDTO taxInfoDTO);

    /**
     * 更新税务信息
     */
    TaxInfoDTO updateTaxInfo(Long id, TaxInfoDTO taxInfoDTO);

    /**
     * 删除税务信息
     */
    void deleteTaxInfo(Long id);

    /**
     * 切换税务信息状态
     */
    TaxInfoDTO toggleStatus(Long id);

    /**
     * 搜索税务信息
     */
    Page<TaxInfoDTO> searchTaxInfos(String query, Pageable pageable);
}
