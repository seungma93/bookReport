package com.example.bookreport.data.entity.response

import com.google.gson.annotations.SerializedName
import java.util.*


data class GoogleBooksResponse (
    @SerializedName("kind") val kind: String? = null,
    @SerializedName("totalItems") val totalItems: Int? = null,
    @SerializedName("items") val items: List<GoogleBooksItemsResponse> = emptyList()
)

data class GoogleBooksItemsResponse(
    @SerializedName("kind") val kind: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("etag") val etag: String? = null,
    @SerializedName("selfLink") val selfLink: String? = null,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfoResponse? = null,
    @SerializedName("userInfo") val userInfo: UserInfoResponse? = null,
    @SerializedName("saleInfo") val saleInfo: SaleInfoResponse? = null,
    @SerializedName("accessInfo") val accessInfo: AccessInfoResponse? = null,
    @SerializedName("searchInfo") val searchInfo: SearchInfoResponse? = null
)

data class VolumeInfoResponse(
    @SerializedName("title") val title: String? = null,
    @SerializedName("subtitle") val subtitle: String? = null,
    @SerializedName("authors") val authors: List<String>? = null,
    @SerializedName("publisher") val publisher: String? = null,
    @SerializedName("publishedDate") val publishedDate: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("industryIdentifiers") val industryIdentifiers: List<IndustryIdentifiersResponse>? = null,
    @SerializedName("pageCount") val pageCount: Int? = null,
    @SerializedName("dimensions") val dimensions: DimensionsResponse?,
    @SerializedName("printType") val printType: String? = null,
    @SerializedName("mainCategory") val mainCategory: String? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("averageRating") val averageRating: Double? = null,
    @SerializedName("ratingsCount") val ratingsCount: Int? = null,
    @SerializedName("contentVersion") val contentVersion: String? = null,
    @SerializedName("imageLinks") val imageLinks: ImageLinksResponse? = null,
    @SerializedName("language") val language: String? = null,
    @SerializedName("previewLink") val previewLink: String? = null,
    @SerializedName("infoLink") val infoLink: String? = null,
    @SerializedName("canonicalVolumeLink") val canonicalVolumeLink: String? = null,
)

enum class Isbn(val value: String) {
    TYPE10("ISBN_10"), TYPE13("ISBN_13")
}

data class IndustryIdentifiersResponse(
    @SerializedName("type") val type: String? = null,
    @SerializedName("identifier") val identifier: String? = null
)
data class DimensionsResponse(
    @SerializedName("height") val height: String? = null,
    @SerializedName("width") val width: String? = null,
    @SerializedName("thickness") val thickness: String? = null
)
data class ImageLinksResponse(
    @SerializedName("smallThumbnail") val smallThumbnail: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("small") val small: String? = null,
    @SerializedName("medium") val medium: String? = null,
    @SerializedName("large") val large: String? = null,
    @SerializedName("extraLarge") val extraLarge: String? = null
)

data class UserInfoResponse(
    @SerializedName("review") val review: String? = null,
    @SerializedName("readingPosition") val readingPosition: String? = null,
    @SerializedName("isPurchased") val isPurchased: Boolean? = null,
    @SerializedName("isPreordered") val isPreordered: Boolean? = null,
    @SerializedName("updated") val updated: Date? = null
)
data class SaleInfoResponse(
    @SerializedName("country") val country: String? = null,
    @SerializedName("saleability") val saleability: String? = null,
    @SerializedName("onSaleDate") val onSaleDate: Date? = null,
    @SerializedName("isEbook") val isEbook: Boolean? = null,
    @SerializedName("listPrice") val listPrice: ListPriceResponse? = null,
    @SerializedName("retailPrice") val retailPrice: RetailPriceResponse? = null,
    @SerializedName("buyLink") val buyLink: String? = null
)

data class ListPriceResponse(
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("currencyCode") val currencyCode: String? = null
)
data class RetailPriceResponse(
    @SerializedName("amount") val amount: Double? = null,
    @SerializedName("currencyCode") val currencyCode: String? = null
)
data class AccessInfoResponse(
    @SerializedName("country") val country: String? = null,
    @SerializedName("viewability") val viewability: String? = null,
    @SerializedName("embeddable") val embeddable: Boolean? = null,
    @SerializedName("publicDomain") val publicDomain: Boolean? = null,
    @SerializedName("textToSpeechPermission") val textToSpeechPermission: String? = null,
    @SerializedName("epub") val epub: EpubResponse? = null,
    @SerializedName("pdf") val pdf: PdfResponse? = null,
    @SerializedName("webReaderLink") val webReaderLink: String? = null,
    @SerializedName("accessViewStatus") val accessViewStatus: String? = null,
    @SerializedName("downloadAccess") val downloadAccess: DownloadAccessResponse? = null
)
data class EpubResponse(
    @SerializedName("isAvailable") val isAvailable: Boolean? = null,
    @SerializedName("downloadLink") val downloadLink: String? = null,
    @SerializedName("acsTokenLink") val acsTokenLink: String? = null
)
data class PdfResponse(
    @SerializedName("isAvailable") val isAvailable: Boolean? = null,
    @SerializedName("downloadLink") val downloadLink: String? = null,
    @SerializedName("acsTokenLink") val acsTokenLink: String? = null
)
data class DownloadAccessResponse(
    @SerializedName("kind") val kind: String? = null,
    @SerializedName("volumeId") val volumeId: String? = null,
    @SerializedName("restricted") val restricted: Boolean? = null,
    @SerializedName("deviceAllowed") val deviceAllowed: Boolean? = null,
    @SerializedName("justAcquired") val justAcquired: Boolean? = null,
    @SerializedName("maxDownloadDevices") val maxDownloadDevices: Int? = null,
    @SerializedName("downloadsAcquired") val downloadsAcquired: Int? = null,
    @SerializedName("nonce") val nonce: String? = null,
    @SerializedName("source") val source: String? = null,
    @SerializedName("reasonCode") val reasonCode: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("signature") val signature: String? = null
)
data class SearchInfoResponse(
    @SerializedName("textSnippet") val textSnippet: String? = null
)
