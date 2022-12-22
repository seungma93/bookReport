package com.example.bookreport.data.entity

import java.util.*


data class GoogleBooksMeta(
    val kind: String,
    val totalItems: Int
)

data class GoogleBooksItems(
    val kind: String,
    val id: String,
    val etag: String,
    val selfLink: String,
    val volumeInfo: VolumeInfo?,
    val userInfo: UserInfo?,
    val saleInfo: SaleInfo?,
    val accessInfo: AccessInfo?,
    val searchInfo: SearchInfo?
)

data class VolumeInfo(
    val title: String,
    val subtitle: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val industryIdentifiers: List<IndustryIdentifiers>?,
    val pageCount: Int,
    val dimensions: Dimensions?,
    val printType: String,
    val mainCategory: String,
    val categories: List<String>,
    val averageRating: Double,
    val ratingsCount: Int,
    val contentVersion: String,
    val imageLinks: ImageLinks?,
    val language: String,
    val previewLink: String,
    val infoLink: String,
    val canonicalVolumeLink: String,
)
data class IndustryIdentifiers(
    val type: String,
    val identifier: String
)
data class Dimensions(
    val height: String,
    val width: String,
    val thickness: String
)
data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String,
    val small: String,
    val medium: String,
    val large: String,
    val extraLarge: String
)

data class UserInfo(
    val review: String, //?
    val readingPosition: String, //?
    val isPurchased: Boolean,
    val isPreordered: Boolean,
    val updated: Date
)
data class SaleInfo(
    val country: String,
    val saleability: String,
    val onSaleDate: Date,
    val isEbook: Boolean,
    val listPrice: ListPrice?,
    val retailPrice: RetailPrice?,
    val buyLink: String
)

data class ListPrice(
    val amount: Double,
    val currencyCode: String
)
data class RetailPrice(
    val amount: Double,
    val currencyCode: String
)
data class AccessInfo(
    val country: String,
    val viewability: String,
    val embeddable: Boolean,
    val publicDomain: Boolean,
    val textToSpeechPermission: String,
    val epub: Epub?,
    val pdf: Pdf?,
    val webReaderLink: String,
    val accessViewStatus: String,
    val downloadAccess: DownloadAccess?
)
data class Epub(
    val isAvailable: Boolean,
    val downloadLink: String,
    val acsTokenLink: String
)
data class Pdf(
    val isAvailable: Boolean,
    val downloadLink: String,
    val acsTokenLink: String
)
data class DownloadAccess(
    val kind: String,
    val volumeId: String,
    val restricted: Boolean,
    val deviceAllowed: Boolean,
    val justAcquired: Boolean,
    val maxDownloadDevices: Int,
    val downloadsAcquired: Int,
    val nonce: String,
    val source: String,
    val reasonCode: String,
    val message: String,
    val signature: String
)
data class SearchInfo(
    val textSnippet: String
)
