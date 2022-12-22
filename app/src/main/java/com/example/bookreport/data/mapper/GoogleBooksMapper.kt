package com.example.bookreport.data.mapper

import com.example.bookreport.data.entity.*
import com.example.bookreport.data.entity.response.*
import java.util.*

fun GoogleBooksResponse.toEntity() = BookEntity.GoogleBooksEntity(
    meta = meta?.toEntity(),
    items = items.map {
        it.toEntity()
    }
)

fun GoogleBooksMetaResponse.toEntity() = GoogleBooksMeta(
    kind = kind.orEmpty(),
    totalItems = totalItems ?: 0
)

fun GoogleBooksItemsResponse.toEntity() = GoogleBooksItems(
    kind  = kind.orEmpty(),
    id = id.orEmpty(),
    etag = etag.orEmpty(),
    selfLink = selfLink.orEmpty(),
    volumeInfo = volumeInfo?.toEntity(),
    userInfo = userInfo?.toEntity(),
    saleInfo = saleInfo?.toEntity(),
    accessInfo = accessInfo?.toEntity(),
    searchInfo = searchInfo?.toEntity()
)

fun VolumeInfoResponse.toEntity() = VolumeInfo(
    title = title.orEmpty(),
    subtitle = subtitle.orEmpty(),
    authors = authors.orEmpty(),
    publisher = publisher.orEmpty(),
    publishedDate = publishedDate.orEmpty(),
    description = description.orEmpty(),
    industryIdentifiers = industryIdentifiers?.map { it.toEntity() },
    pageCount = pageCount ?: 0,
    dimensions = dimensions?.toEntity(),
    printType = printType.orEmpty(),
    mainCategory = mainCategory.orEmpty(),
    categories = categories.orEmpty(),
    averageRating = averageRating ?: 0.0,
    ratingsCount = ratingsCount ?: 0,
    contentVersion = contentVersion.orEmpty(),
    imageLinks = imageLinks?.toEntity(),
    language = language.orEmpty(),
    previewLink = previewLink.orEmpty(),
    infoLink = infoLink.orEmpty(),
    canonicalVolumeLink = canonicalVolumeLink.orEmpty()
    )

fun IndustryIdentifiersResponse.toEntity() = IndustryIdentifiers(
    type = type.orEmpty(),
    identifier = identifier.orEmpty()
)

fun DimensionsResponse.toEntity() = Dimensions(
    height = height.orEmpty(),
    width = width.orEmpty(),
    thickness = thickness.orEmpty()
)

fun ImageLinksResponse.toEntity() = ImageLinks(
    smallThumbnail = smallThumbnail.orEmpty(),
    thumbnail = thumbnail.orEmpty(),
    small = small.orEmpty(),
    medium = medium.orEmpty(),
    large = large.orEmpty(),
    extraLarge = extraLarge.orEmpty()
)

fun UserInfoResponse.toEntity() = UserInfo(
    review = review.orEmpty(),
    readingPosition = readingPosition.orEmpty(),
    isPurchased = isPurchased ?: true,
    isPreordered = isPreordered ?: true,
    updated = updated ?: Date() // ?????????
)

fun SaleInfoResponse.toEntity() = SaleInfo(
    country = country.orEmpty(),
    saleability = saleability.orEmpty(),
    onSaleDate = onSaleDate ?: Date(), //?????????
    isEbook = isEbook ?: true,
    listPrice = listPrice?.toEntity(),
    retailPrice = retailPrice?.toEntity(),
    buyLink = buyLink.orEmpty()
)

fun ListPriceResponse.toEntity() = ListPrice(
    amount = amount ?: 0.0,
    currencyCode = currencyCode.orEmpty()
)

fun RetailPriceResponse.toEntity() = RetailPrice(
    amount = amount ?: 0.0,
    currencyCode = currencyCode.orEmpty()
)

fun AccessInfoResponse.toEntity() = AccessInfo(
    country = country.orEmpty(),
    viewability = viewability.orEmpty(),
    embeddable = embeddable ?: true,
    publicDomain = publicDomain ?: true,
    textToSpeechPermission = textToSpeechPermission.orEmpty(),
    epub = epub?.toEntity(),
    pdf = pdf?.toEntity(),
    webReaderLink = webReaderLink.orEmpty(),
    accessViewStatus = accessViewStatus.orEmpty(),
    downloadAccess = downloadAccess?.toEntity()
)

fun EpubResponse.toEntity() = Epub(
    isAvailable = isAvailable ?: true,
    downloadLink = downloadLink.orEmpty(),
    acsTokenLink = acsTokenLink.orEmpty()
)

fun PdfResponse.toEntity() = Pdf(
    isAvailable = isAvailable ?: true,
    downloadLink = downloadLink.orEmpty(),
    acsTokenLink = acsTokenLink.orEmpty()
)

fun DownloadAccessResponse.toEntity() = DownloadAccess(
    kind = kind.orEmpty(),
    volumeId = volumeId.orEmpty(),
    restricted = restricted ?: true,
    deviceAllowed = deviceAllowed ?: true,
    justAcquired = justAcquired ?: true,
    maxDownloadDevices = maxDownloadDevices ?: 0,
    downloadsAcquired = downloadsAcquired ?: 0,
    nonce = nonce.orEmpty(),
    source = source.orEmpty(),
    reasonCode = reasonCode.orEmpty(),
    message = message.orEmpty(),
    signature = signature.orEmpty()
)

fun SearchInfoResponse.toEntity() = SearchInfo(
    textSnippet = textSnippet.orEmpty()
)