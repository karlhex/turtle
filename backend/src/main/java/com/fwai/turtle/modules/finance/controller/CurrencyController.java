package com.fwai.turtle.modules.finance.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.finance.dto.CurrencyDTO;
import com.fwai.turtle.modules.finance.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    /**
     * 获取货币列表（分页）
     *
     * @param pageable 分页参数
     * @param active 是否启用
     * @return 分页货币列表
     */
    @GetMapping
    public ApiResponse<Page<CurrencyDTO>> getCurrencies(Pageable pageable, @RequestParam(required = false) Boolean active) {
        Page<CurrencyDTO> page = active != null && active ? 
            currencyService.getAllActive(pageable) : 
            currencyService.getAll(pageable);
        return ApiResponse.ok(page);
    }

    /**
     * 根据ID获取货币
     *
     * @param id 货币ID
     * @return 货币信息
     */
    @GetMapping("/{id}")
    public ApiResponse<CurrencyDTO> getCurrency(@PathVariable Long id) {
        CurrencyDTO currency = currencyService.getById(id);
        return ApiResponse.ok(currency);
    }

    /**
     * 创建货币
     *
     * @param currencyDTO 货币信息
     * @return 创建的货币信息
     */
    @PostMapping
    public ApiResponse<CurrencyDTO> createCurrency(@RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO created = currencyService.create(currencyDTO);
        return ApiResponse.ok(created);
    }

    /**
     * 更新货币
     *
     * @param id 货币ID
     * @param currencyDTO 货币信息
     * @return 更新后的货币信息
     */
    @PutMapping("/{id}")
    public ApiResponse<CurrencyDTO> updateCurrency(@PathVariable Long id, @RequestBody CurrencyDTO currencyDTO) {
        CurrencyDTO updated = currencyService.update(id, currencyDTO);
        return ApiResponse.ok(updated);
    }

    /**
     * 删除货币
     *
     * @param id 货币ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.delete(id);
        return ApiResponse.ok(null);
    }

    /**
     * 切换货币状态（启用/禁用）
     *
     * @param id 货币ID
     * @return 更新后的货币信息
     */
    @PutMapping("/{id}/toggle-status")
    public ApiResponse<CurrencyDTO> toggleStatus(@PathVariable Long id) {
        // 这个方法需要在CurrencyService中实现
        throw new UnsupportedOperationException("Toggle status not implemented yet");
    }

    /**
     * 设置为基准货币
     *
     * @param id 货币ID
     * @return 更新后的货币信息
     */
    @PutMapping("/{id}/set-base")
    public ApiResponse<Void> setBaseCurrency(@PathVariable Long id) {
        currencyService.setBaseCurrency(id);
        return ApiResponse.ok(null);
    }
}
