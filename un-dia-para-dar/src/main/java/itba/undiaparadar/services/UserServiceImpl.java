package itba.undiaparadar.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.User;
import itba.undiaparadar.utils.ProfilePhotoAsync;

public class UserServiceImpl implements UserService {
	private User user = new User();

	@Override
	public void saveNewUser() {
		final ParseUser parseUser = ParseUser.getCurrentUser();
		parseUser.setUsername(user.getName());
		parseUser.setEmail(user.getEmail());
//        Saving profile photo as a ParseFile
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap picture = user.getPicture();
		picture.compress(Bitmap.CompressFormat.JPEG, 70, stream);
		byte[] data = stream.toByteArray();
		String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
		final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);
		parseFile.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				parseUser.put("profileThumb", parseFile);
				//Finally save all the user details
				parseUser.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						Toast.makeText(UnDiaParaDarApplication.getAppContext(),
								"New user:" + user.getName() + " Signed up", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public void getUserDetailsFromFB() {
		Bundle parameters = new Bundle();
		parameters.putString("fields", "email,name,picture,objectId");
		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/me",
				parameters,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
         /* handle the result */
						try {
							String userId = response.getJSONObject().getString("objectId");
							user.setUserId(userId);
							String email = response.getJSONObject().getString("email");
							user.setEmail(email);
							String name = response.getJSONObject().getString("name");
							user.setName(name);
							JSONObject picture = response.getJSONObject().getJSONObject("picture");
							JSONObject data = picture.getJSONObject("data");
							//  Returns a 50x50 profile picture
							String pictureUrl = data.getString("url");
							user.setPictureUrl(pictureUrl);
							new ProfilePhotoAsync(pictureUrl).execute();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
		).executeAsync();
	}

	@Override
	public void getUserDetailsFromParse() {
		ParseUser parseUser = ParseUser.getCurrentUser();
		//Fetch profile photo
		try {
			ParseFile parseFile = parseUser.getParseFile("profileThumb");
			byte[] data = parseFile.getData();
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			user.setPicture(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		user.setName(parseUser.getEmail());
		user.setEmail(parseUser.getUsername());
	}
}
