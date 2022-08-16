# online-fashion-shop-app
> Online fashion shopping application for android by Kyungha Kim and Seungwoo Choo. <br/><br/>
This application consists of 3 fragments. <br/>
The first is [Products Tab](#products-tab), the second is [Hot Tab](#hot-tab) and the last is [UserInfo Tab](#userinfo-tab).

#### Front-end
- Kyungha Kim
#### Back-end + KaKao/Google SDK
- Seungwoo Choo

<br/>

![splash](https://user-images.githubusercontent.com/65812107/135451877-bfa4569a-f654-4f55-8b81-37cc8e4d13bd.gif)

<br/>

## Development Environment
> This project was developed for a week.

### Client
- Android Studio
- Java

### Server
- JavaScript
- Node.js
- Express

### Database
- Mysql

<br/>

## Dependencies Used
- ```Kakao SDK``` for login
- ```Google SDK``` for login
- ```Retrofit2``` for HTTP
- ```Glide``` for image loading

<br/>

## Usage
- Download or clone the project
- Compile on Android studio or use apk

<br/>

## Features

### Login page
- Sign in and Log in with email + password (Password is encrypted by sha512)
- Login with Google or Kakao account (Automatically Sign in)

![signup](https://user-images.githubusercontent.com/65812107/135451718-18d81034-b505-4edf-afeb-cb41e6d12240.gif)
![login](https://user-images.githubusercontent.com/65812107/135451729-5e426b00-2d21-4293-8776-6e85603ae4fa.gif)
![google_login](https://user-images.githubusercontent.com/68063560/184792704-94b5c77f-aa06-49ea-9a5c-6ccb77d1e48f.gif)

<br/>

### Login result page
- Profile image, email and user name (If there is no profile image, show default image)
- Go to [Tab](#products-tab)
- Logout
- Revoke Google Access (Only works when logged in with google account)

![gotothetab](https://user-images.githubusercontent.com/65812107/135451735-bb85d8b8-867b-4a2f-89a7-659e416ef78b.gif)
![logout](https://user-images.githubusercontent.com/65812107/135451728-b0db6ed6-afd0-4660-b692-12c50cdee9cd.gif)

<br/>

### Products Tab
- Main and Sub categories of products
- Products of each sub categories
- Move to [Image Full Activity](#image-full-activity) by clicking product.

![categories](https://user-images.githubusercontent.com/65812107/135451740-0ea4aae4-6af4-43b5-a3e2-695aad52578a.gif)

<br/>

### Hot Tab
- Products are sorted by a number of hits

![hottab](https://user-images.githubusercontent.com/65812107/135451731-10b3dc78-477f-4a4d-91c8-63bd2fb0e7dd.gif)

<br/>

### UserInfo Tab
- Profile image, email, user name, and amount of cash
- A list of purchased products with quantity and time of purchase

![purchaselist](https://user-images.githubusercontent.com/68063560/184795640-f99564d0-7e58-4944-80cd-9cabf24be40f.gif)

<br/>

### Image Full Activity
- Image, name, price and available stock quantity of selected product
- Select the amount of items you want to buy
- Add To [Cart](#cart-floating-action)

![addtocart](https://user-images.githubusercontent.com/65812107/135451744-32f422df-0135-44a9-b1f8-ce93a77943ff.gif)

<br/>

### Cart Floating Action
- Cart button can be found in the bottom right corner of every tab
- Total Price
- A list of products added to the cart
- Buy products (Cannot be processed if the amount of cash is less than total price)
- Delete products by long click

![buyall](https://user-images.githubusercontent.com/65812107/135451741-d4d4a062-5d4e-41c1-aa2a-975e904f6812.gif)
![ezgif-3-0599e32740f7](https://user-images.githubusercontent.com/65812107/135451726-f13fd109-1d39-4ce9-9618-53e7fbb84eff.gif)
![delete](https://user-images.githubusercontent.com/65812107/135451737-9571c0eb-82f9-489a-aa77-56c8286d2104.gif)
