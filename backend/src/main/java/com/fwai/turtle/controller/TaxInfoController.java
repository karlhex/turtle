package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.TaxInfoDTO;
import com.fwai.turtle.service.interfaces.TaxInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tax-infos")
@RequiredArgsConstructor
public class TaxInfoController {

    private final TaxInfoService taxInfoService;

    /**
     * 获取税务信息列表（分页）
     *
     * @param pageable 分页参数
     * @param active 是否启用
     * @return 分页税务信息列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaxInfoDTO>>> getTaxInfos(
            Pageable pageable,
            @RequestParam(required = false) Boolean active) {
        Page<TaxInfoDTO> page = taxInfoService.getTaxInfos(pageable, active);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    /**
     * 搜索税务信息
     *
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TaxInfoDTO>>> searchTaxInfos(
            @RequestParam String query,
            Pageable pageable) {
        Page<TaxInfoDTO> page = taxInfoService.searchTaxInfos(query, pageable);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    /**
     * 获取所有启用的税务信息列表
     *
     * @return 所有启用的税务信息列表
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<TaxInfoDTO>>> getActiveTaxInfos() {
        List<TaxInfoDTO> taxInfos = taxInfoService.getAllActiveTaxInfos();
        return ResponseEntity.ok(ApiResponse.ok(taxInfos));
    }

    /**
     * 根据ID获取税务信息
     *
     * @param id 税务信息ID
     * @return 税务信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxInfoDTO>> getTaxInfo(@PathVariable Long id) {
        TaxInfoDTO taxInfo = taxInfoService.getTaxInfo(id);
        return ResponseEntity.ok(ApiResponse.ok(taxInfo));
    }

    /**
     * 创建税务信息
     *
     * @param taxInfoDTO 税务信息
     * @return 创建的税务信息
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TaxInfoDTO>> createTaxInfo(@RequestBody TaxInfoDTO taxInfoDTO) {
        TaxInfoDTO created = taxInfoService.createTaxInfo(taxInfoDTO);
        return ResponseEntity.ok(ApiResponse.ok(created));
    }

    /**
     * 更新税务信息
     *
     * @param id 税务信息ID
     * @param taxInfoDTO 税务信息
     * @return 更新后的税务信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaxInfoDTO>> updateTaxInfo(
            @PathVariable Long id,
            @RequestBody TaxInfoDTO taxInfoDTO) {
        TaxInfoDTO updated = taxInfoService.updateTaxInfo(id, taxInfoDTO);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    /**
     * 删除税务信息
     *
     * @param id 税务信息ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTaxInfo(@PathVariable Long id) {
        taxInfoService.deleteTaxInfo(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * 切换税务信息状态（启用/禁用）
     *
     * @param id 税务信息ID
     * @return 更新后的税务信息
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<TaxInfoDTO>> toggleStatus(@PathVariable Long id) {
        TaxInfoDTO updated = taxInfoService.toggleStatus(id);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}
