package org.example.minishop.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.example.minishop.util.Category;


@Converter(autoApply = true)
public class CategoryConverter implements AttributeConverter<Category, String> {


    @Override
    public String convertToDatabaseColumn(Category category) {
        if (category == null) {
            return null;
        }
        return category.getDisplayName();
    }

    @Override
    public Category convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        for (Category category : Category.values()) {
            if (category.getDisplayName().equalsIgnoreCase(dbData)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unknown category: " + dbData);
    }

}
