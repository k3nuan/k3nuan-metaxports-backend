# API Description

## base access `/api/v1`

## base **user_info** :

```
{
    id: user_id,
    phone: phone_number,
    role: "ROLE NAME",
    status: "STATUS NAME",
    username: "user name",
    birthDate: "YYYY-MM-DD",
    gender: "female|male|open_value"
    profilePic: "profile pic",
    lastLogin: "YYYY-MM-DDTHH:mm:ss",
   }
```

Users
-----

* Login `POST /users/login`

  Request with Auth

  Response a user_info

* Logout `Delete /users/logout`

* Change Password `POST /users/password`

  Request
  ```
  { password: "newPassword", passwordConfirm: "newPassword", oldPassword: "oldPassword" }
  ```

* Email availability `GET /users/emails/{email}/available?forRole=role_name`

  Response a boolean value

* Find User Address `GET /user/address`

  Response
  ```
  { street: "ST Name", suite: "suite" (optional), city: "city name", state: { key: "STATE_KEY", value: "State Name" }, zipCode: zip_code, country: { id: country_id, value: "Country Name" } }
  ```

* Find Profile Pic `GET /users/profile/img`

  Response the file data if exist or no-content

* Update Profile Data `POST /users/profile`

  Request
  ```
  {
    firstName: "firstname", lastName: "lastname", phone: "phone", gender: "male|female|open_value",
    address: {
      street: "street", suite: "suite" (optional),
      country: { id: 1 }, state: { key: "STATE_KEY" },
      city: "city", zipCode: zip_code
    }
  }
  ```

* Upload Profile Pic `PUT /users/profile/img`

  Request (form-data)

  pic: image_file

  Response a user_info


Events
------


Catalogs
--------
* Countries `GET /catalogs/countries`

  return an array of caountries with
  ```
  { key: "COUNTRY KEY", value: "country name" }
  ```


* States `GET /catalogs/states?country=country_id`

  return an array of states with
  ```
  { key: "STATE KEY", value: "state name" }
  ```
