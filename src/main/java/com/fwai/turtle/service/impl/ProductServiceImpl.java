package com.fwai.turtle.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fwai.turtle.dto.ProductDTO;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.mapper.ProductMapper;
import com.fwai.turtle.persistence.repository.ProductRepository;
import com.fwai.turtle.service.interfaces.ProductService;
import com.fwai.turtle.types.ProductType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        if (productRepository.existsByNameAndModelNumber(productDTO.getName(), productDTO.getModelNumber())) {
            throw new DuplicateRecordException("相同名称和型号的产品已存在");
        }

        Product product = productMapper.toEntity(productDTO);
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

        productMapper.updateEntity(productDTO, product);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
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
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("产品不存在"));
        return productMapper.toDTO(product);
    }

    @Override
    public Page<ProductDTO> getAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getAllActive(Pageable pageable) {
        return productRepository.findByActive(true, pageable).map(productMapper::toDTO);
    }

    @Override
    public Page<ProductDTO> search(String query, Pageable pageable) {
        if (!StringUtils.hasText(query)) {
            return getAll(pageable);
        }
        
        return productRepository.findByNameContainingIgnoreCaseOrModelNumberContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
                query, query, query, pageable)
                .map(productMapper::toDTO);
    }

    @Override
    public Page<ProductDTO> getByType(ProductType type, Pageable pageable) {
        return productRepository.findByType(type, pageable).map(productMapper::toDTO);
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
