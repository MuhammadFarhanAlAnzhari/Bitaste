package com.zhari.bitaste.data.datasource

import com.zhari.bitaste.model.Menu


/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface ProductDataSource {
    fun getProductList(): List<Menu>
}

class ProductDataSourceImpl() : ProductDataSource {
    override fun getProductList(): List<Menu> = listOf(
        Menu(
            name = "Ayam Bakar",
            price = 18000.0,
            rating = 4.8,
            desc = "Ayam bakar adalah hidangan Indonesia yang terdiri dari potongan ayam yang telah direndam dalam bumbu rempah khas Indonesia, \n" +
                    "kemudian dipanggang atau dibakar di atas arang.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_ayam_bakar.jpg"
        ),Menu(
            name = "Ayam Goreng",
            price = 18000.0,
            rating = 4.7,
            desc = "Ayam goreng adalah hidangan populer yang berasal dari Indonesia.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_ayam_goreng.jpg"
        ),Menu(
            name = "Burger",
            price = 18000.0,
            rating = 4.5,
            desc = "Burger adalah makanan yang terdiri dari sepotong daging cincang, biasanya sapi, yang dibentuk menjadi patty " +
                    "dan dimasak, seringkali digoreng, bakar, atau panggang. Patty daging ini kemudian diletakkan di antara irisan roti bundar yang disebut bun.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_burger.jpg"
        ),Menu(
            name = "Spaghetti Keju",
            price = 18000.0,
            rating = 4.6,
            desc = "Spaghetti keju adalah salah satu jenis hidangan pasta yang terkenal, yang merupakan bagian dari masakan Italia. " +
                    "Hidangan ini terdiri dari spaghetti yang dimasak al dente (tidak terlalu lembek), yang kemudian disajikan dengan saus keju yang kaya dan lezat.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_cheese_spaghetti.jpg"
        ),Menu(
            name = "Ayam Bawang",
            price = 18000.0,
            rating = 4.7,
            desc = "Ayam Bawang adalah ayam yang dimarinasi dengan bawang yang menciptakan rasa khusus.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_chicken_garlic.jpg"
        ),Menu(
            name = "Kopi",
            price = 18000.0,
            rating = 4.8,
            desc = "Kopi adalah minuman yang dibuat dari biji kopi yang telah digiling dan diseduh dengan air panas. " +
                    "Proses pembuatan kopi melibatkan penggilingan biji kopi yang telah dipanggang untuk menghasilkan bubuk kopi yang kemudian diseduh dalam air panas. ",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_coffee.jpg"
        ),Menu(
            name = "Dimsum",
            price = 18000.0,
            rating = 4.8,
            desc = "Dimsum adalah sejenis hidangan kecil yang berasal dari masakan Tionghoa. Biasanya dimsum disajikan " +
                    "dalam porsi kecil dan dimakan sebagai makanan ringan atau sarapan. Makanan ini dapat berupa berbagai " +
                    "jenis hidangan seperti dumpling, siomay, bakpao, pangsit, dan berbagai jenis hidangan kukus " +
                    "atau digoreng lainnya.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_dimsum.jpg"
        ),
        Menu(
            name = "Kentang Goreng",
            price = 18000.0,
            rating = 4.4,
            desc = "Kentang goreng adalah hidangan yang terbuat dari potongan-potongan kentang yang digoreng " +
                    "dalam minyak panas hingga menjadi garing dan berwarna keemasan.",
            menuImgUrl = "https://raw.githubusercontent.com/MuhammadFarhanAlAnzhari/challenge-assets/main/menus/img_frice_potato.jpg"
        ),


    )
}