package com.fwai.turtle.modules.customer.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fwai.turtle.modules.customer.dto.ProductDTO;
import com.fwai.turtle.base.exception.DuplicateRecordException;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.customer.entity.Product;
import com.fwai.turtle.modules.customer.mapper.ProductMapper;
import com.fwai.turtle.modules.customer.entity.Company;
import com.fwai.turtle.modules.customer.repository.ProductRepository;
import com.fwai.turtle.modules.customer.repository.CompanyRepository;
import com.fwai.turtle.modules.customer.service.ProductService;
import com.fwai.turtle.base.types.ProductType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        if (productRepository.existsByNameAndModelNumber(productDTO.getName(), productDTO.getModelNumber())) {
            throw new DuplicateRecordException("相同名称和型号的产品已存在");
        }

        // Verify manufacturer exists
        if (productDTO.getManufacturer() == null || productDTO.getManufacturer().getId() == null) {
            throw new IllegalArgumentException("制造商信息不能为空");
        }
        
        Company manufacturer = companyRepository.findById(productDTO.getManufacturer().getId())
            .orElseThrow(() -> new ResourceNotFoundException("制造商不存在"));

        Product product = productMapper.toEntity(productDTO);
        product.setManufacturer(manufacturer);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在"));

        // 检查是否存在同名同型号的其他产品
        if (!product.getName().equals(productDTO.getName()) || 
            !product.getModelNumber().equals(productDTO.getModelNumber())) {
            if (productRepository.existsByNameAndModelNumber(
                    productDTO.getName(), productDTO.getModelNumber())) {
                throw new DuplicateRecordException("相同名称和型号的产品已存在");
            }
        }

        // Verify manufacturer exists
        if (productDTO.getManufacturer() == null || productDTO.getManufacturer().getId() == null) {
            throw new IllegalArgumentException("制造商信息不能为空");
        }
        
        Company manufacturer = companyRepository.findById(productDTO.getManufacturer().getId())
            .orElseThrow(() -> new ResourceNotFoundException("制造商不存在"));

        productMapper.updateEntityFromDTO(productDTO, product);
        product.setManufacturer(manufacturer);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> search(String query, Pageable pageable) {
        if (!StringUtils.hasText(query)) {
            return getAll(pageable);
        }
        return productRepository.searchProducts(query, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllActive(Pageable pageable) {
        return productRepository.findByActive(true, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getByType(ProductType type, Pageable pageable) {
        return productRepository.findByType(type, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("产品不存在");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void toggleStatus(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在"));
        product.setActive(!product.getActive());
        productRepository.save(product);
    }
}
