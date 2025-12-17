package com.devoops.rentalbrain.product.productlist.command.service;

import com.devoops.rentalbrain.common.codegenerator.CodeGenerator;
import com.devoops.rentalbrain.common.codegenerator.CodeType;
import com.devoops.rentalbrain.product.productlist.command.dto.ItemDTO;
import com.devoops.rentalbrain.product.productlist.command.dto.ModifyItemDTO;
import com.devoops.rentalbrain.product.productlist.command.entity.Item;
import com.devoops.rentalbrain.product.productlist.command.entity.ItemCategory;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemCategoryRepository;
import com.devoops.rentalbrain.product.productlist.command.repository.ItemRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemCommandServiceImpl implements ItemCommandService {
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public ItemCommandServiceImpl(ModelMapper modelMapper,
                                  ItemRepository itemRepository,
                                  ItemCategoryRepository itemCategoryRepository, CodeGenerator codeGenerator) {
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional
    public void insertNewItem(ItemDTO itemDTO) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        String categoryName = itemDTO.getCategoryName();
        ItemCategory itemCategory = itemCategoryRepository.findAllByName(categoryName);
        Long categoryId = itemCategory.getId();
        Item item = modelMapper.map(itemDTO, Item.class);

        String itemCode = codeGenerator.generate(CodeType.ITEM);
        item.setItemCode(itemCode);
        item.setStatus("P");
        item.setSales(0);
        item.setRepairCost(0);
        item.setCategoryId(categoryId);

        itemRepository.save(item);
    }

    @Override
    @Transactional
    public String updateItem(int itemId, ModifyItemDTO itemDTO) {
        Item item = itemRepository.findById((long)itemId).get();
        if(itemDTO.getName() != null && !item.getName().equals(itemDTO.getName())) {
            item.setName(itemDTO.getName());
        }
        if(itemDTO.getMonthlyPrice() != null && !item.getMonthlyPrice().equals(itemDTO.getMonthlyPrice())) {
            item.setMonthlyPrice(itemDTO.getMonthlyPrice());
        }
        if(itemDTO.getStatus() != null && !item.getStatus().equals(itemDTO.getStatus())) {
            item.setStatus(itemDTO.getStatus());
        }
        if(itemDTO.getLastInspectDate() != null && !item.getLastInspectDate().equals(itemDTO.getLastInspectDate())) {
            item.setLastInspectDate(itemDTO.getLastInspectDate());
        }
        if(itemDTO.getSales() != null && !item.getSales().equals(itemDTO.getSales())) {
            item.setSales(itemDTO.getSales());
        }
        if(itemDTO.getRepairCost() != null && !item.getRepairCost().equals(itemDTO.getRepairCost())) {
            item.setRepairCost(itemDTO.getRepairCost());
        }

        if(itemDTO.getCategoryName() != null) {
        String categoryName = itemDTO.getCategoryName();
        ItemCategory itemCategory = itemCategoryRepository.findAllByName(categoryName);

        Long categoryId = itemCategory.getId();
        if(!item.getCategoryId().equals(categoryId)) {
            item.setCategoryId(categoryId);
        }
        }

        itemRepository.save(item);

        return "item update success";
    }

    @Override
    @Transactional
    public String deleteItem(int itemId) {
        itemRepository.deleteById((long)itemId);
        return "item delete success";
    }
}
