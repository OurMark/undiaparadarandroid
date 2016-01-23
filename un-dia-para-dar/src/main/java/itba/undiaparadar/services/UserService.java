package itba.undiaparadar.services;

import itba.undiaparadar.model.User;

public interface UserService {

	User getUser();

	void getUserDetailsFromFB();

	void saveNewUser();

	void getUserDetailsFromParse();
}
