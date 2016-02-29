package joshie.enchiridion.books.features.actions;

import com.google.gson.JsonObject;

import joshie.enchiridion.api.EnchiridionAPI;
import joshie.enchiridion.api.IButtonAction;
import joshie.enchiridion.api.IPage;
import joshie.enchiridion.books.BookRegistry;
import joshie.enchiridion.books.gui.GuiBook;
import joshie.enchiridion.helpers.JumpHelper;
import joshie.lib.helpers.JSONHelper;

public class ActionJumpPage extends AbstractAction {
	public transient IPage page;
	public transient String bookID;
	public transient String name;
	public transient int pageNumber;
	
	public ActionJumpPage() {
		super("jump");
	}
	
	@Override
	public IButtonAction create() {
		ActionJumpPage jump = new ActionJumpPage(EnchiridionAPI.book.getPage());
		jump.bookID = EnchiridionAPI.book.getBook().getUniqueName();
		return jump;
	}
	
	public ActionJumpPage(IPage page) {
		this();
		this.name = page.getPageName();
		this.pageNumber = page.getPageNumber();
		this.page = page;
	}
	
	@Override
	public String[] getFieldNames() {
		return new String[] { "tooltip", "bookID", "pageNumber" };
	}
	
	@Override
	public void performAction() {	
		if (bookID != null && !bookID.equals("") && !bookID.equals(EnchiridionAPI.book.getBook().getUniqueName())) {
			GuiBook.INSTANCE.setBook(BookRegistry.INSTANCE.getBookByName(bookID), EnchiridionAPI.book.isEditMode());
		}
		
		if (page == null) {
			page = JumpHelper.getPageByNumber(pageNumber);
			if (page == null) page = JumpHelper.getPageByName(name);
		}
				
		EnchiridionAPI.book.setPage(page);
	}

	@Override
	public void readFromJson(JsonObject json) {
		super.readFromJson(json);
		bookID = JSONHelper.getStringIfExists(json, "bookID");
		pageNumber = JSONHelper.getIntegerIfExists(json, "number");
		name = JSONHelper.getStringIfExists(json, "name");
	}

	@Override
	public void writeToJson(JsonObject object) {
		super.writeToJson(object);
		object.addProperty("bookID", bookID);
		if (page != null) {
			object.addProperty("number", page.getPageNumber());
			object.addProperty("name", page.getPageName());
		}
	}
}
