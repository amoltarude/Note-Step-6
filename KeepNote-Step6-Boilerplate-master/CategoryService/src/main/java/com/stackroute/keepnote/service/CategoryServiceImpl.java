package com.stackroute.keepnote.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.CategoryDoesNoteExistsException;
import com.stackroute.keepnote.exception.CategoryNotCreatedException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.repository.CategoryRepository;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */
@Service
public class CategoryServiceImpl implements CategoryService {

	/*
	 * Autowiring should be implemented for the CategoryRepository. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
	//	super();
		this.categoryRepository = categoryRepository;
	}
	/*
	 * This method should be used to save a new category.Call the corresponding
	 * method of Respository interface.
	 */
	public Category createCategory(Category category) throws CategoryNotCreatedException {

		Category categoryCreated =  categoryRepository.insert(category);
		
		if(categoryCreated!=null)
		{
			return categoryCreated;
		}
		throw new CategoryNotCreatedException("CategoryNotCreatedException");
	
	}

	/*
	 * This method should be used to delete an existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public boolean deleteCategory(String categoryId) throws CategoryDoesNoteExistsException {

		try {
			Category category = getCategoryById(categoryId);
			if (category!=null) {
				categoryRepository.deleteById(categoryId);
				return true;
			}else {
				throw new CategoryDoesNoteExistsException("CategoryDoesNoteExistsException");
//		return false;		
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;		}
	}

	/*
	 * This method should be used to update a existing category.Call the
	 * corresponding method of Respository interface.
	 */
	public Category updateCategory(Category category, String categoryId) {
		try {
			Category categoryById = getCategoryById(categoryId);
			if (categoryById!=null) {
				categoryRepository.save(category);
				return categoryRepository.findById(categoryId).get();
			}
		} catch (CategoryNotFoundException e) {

			}
		return categoryRepository.findById(categoryId).get();
	}

	/*
	 * This method should be used to get a category by categoryId.Call the
	 * corresponding method of Respository interface.
	 */
	public Category getCategoryById(String categoryId) throws CategoryNotFoundException {

		try {
			Optional<Category> findById = categoryRepository.findById(categoryId);
			return findById.orElseThrow(()-> new CategoryNotFoundException("CategoryNotFoundException"));
		} catch (NoSuchElementException  e) {
			throw new CategoryNotFoundException("CategoryNotFoundException");
			
		}
	}

	/*
	 * This method should be used to get a category by userId.Call the corresponding
	 * method of Respository interface.
	 */
	public List<Category> getAllCategoryByUserId(String userId) {
		return categoryRepository.findAllCategoryByCategoryCreatedBy(userId);
	}

}
