package by.bsuir.store.controller.command.impl.admin;

import by.bsuir.store.controller.command.ICommand;
import by.bsuir.store.domain.User;
import by.bsuir.store.resource.ConfigurationManager;
import by.bsuir.store.resource.MessageManager;
import by.bsuir.store.service.UserService;
import by.bsuir.store.service.impl.UserServiceImpl;
import by.bsuir.store.service.ServiceException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AddToBlacklistCommand implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(AddToBlacklistCommand.class);
    private static final String PARAM_NAME_USER_ID = "id";
    private static final String USERS = "users";
    private static final String URL = "url";
    private static final String MESSAGE = "message";
    private static final String USERS_ADMIN_PAGE = "path.page.admin.users";
    private static final String ERROR_PAGE = "path.page.error";

    @Override
    public String execute(HttpServletRequest request) {
        String page = null;
        int id = Integer.parseInt(request.getParameter(PARAM_NAME_USER_ID));
        UserService userService = UserServiceImpl.getInstance();
        try {
            userService.addToBlackList(id);
            List<User> users = userService.allUsers();
            if (!users.isEmpty()) {
                request.getSession().setAttribute(USERS, users);
                page = ConfigurationManager.getProperty(USERS_ADMIN_PAGE);
                request.getSession().setAttribute(URL, page);
            } else {
                request.setAttribute(MESSAGE, MessageManager.EMPTY_USER_LIST);
                page = ConfigurationManager.getProperty(USERS_ADMIN_PAGE);
                request.getSession().setAttribute(URL, page);
            }
        } catch (ServiceException e) {
            LOGGER.error("Error during adding user to blacklist: " + e);
            request.setAttribute(MESSAGE, MessageManager.DATABASE_ERROR);
            page = ConfigurationManager.getProperty(ERROR_PAGE);
        }
        return page;
    }
}
