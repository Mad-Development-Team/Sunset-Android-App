package com.madteam.sunset.ui.screens.post.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.madteam.sunset.R
import com.madteam.sunset.data.model.SpotPost
import com.madteam.sunset.ui.common.AutoSlidingCarousel
import com.madteam.sunset.ui.common.CustomSpacer
import com.madteam.sunset.ui.common.GoBackTopAppBar
import com.madteam.sunset.ui.common.ProfileImage
import com.madteam.sunset.ui.common.RoundedLightGoToSpotButton
import com.madteam.sunset.ui.common.RoundedLightLikeButton
import com.madteam.sunset.ui.common.RoundedLightSaveButton
import com.madteam.sunset.ui.common.RoundedLightSendButton
import com.madteam.sunset.ui.screens.post.state.PostUIEvent
import com.madteam.sunset.ui.screens.post.state.PostUIState
import com.madteam.sunset.ui.screens.post.viewmodel.PostViewModel
import com.madteam.sunset.ui.theme.primaryBoldHeadlineXS
import com.madteam.sunset.ui.theme.secondaryRegularBodyM
import com.madteam.sunset.ui.theme.secondaryRegularBodyS
import com.madteam.sunset.ui.theme.secondarySemiBoldBodyM
import com.madteam.sunset.utils.formatDate
import com.madteam.sunset.utils.generateDeepLink
import com.madteam.sunset.utils.getShareIntent
import com.madteam.sunset.utils.shimmerBrush

@Composable
fun PostScreen(
    viewModel: PostViewModel = hiltViewModel(),
    postReference: String,
    navController: NavController
) {

    viewModel.onEvent(PostUIEvent.SetPostReference("posts/$postReference"))
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            GoBackTopAppBar(title = R.string.spot_post_title) {
                navController.popBackStack()
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                PostContent(
                    state = state,
                    navigateTo = navController::navigate,
                    postLikeClick = { viewModel.onEvent(PostUIEvent.ModifyUserPostLike) }
                )
            }
        }
    )
}

@OptIn(ExperimentalPagerApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun PostContent(
    state: PostUIState,
    navigateTo: (String) -> Unit,
    postLikeClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val showShimmer = remember { mutableStateOf(true) }
    val context = LocalContext.current

    val deepLinkToShare = generateDeepLink(screen = "post", state.postInfo.id)
    val shareText = stringResource(
        id = R.string.share_post_text,
        state.postInfo.author.username
    ) + " $deepLinkToShare"

    if (state.postInfo != SpotPost()) {
        showShimmer.value = false
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
    ) {
        //Header image section
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotImage, saveIconButton, sendIconButton, likeIconButton, spotIconButton) = createRefs()
            AutoSlidingCarousel(
                itemsCount = state.postInfo.images.size,
                itemContent = { index ->
                    GlideImage(
                        model = state.postInfo.images[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.background(shimmerBrush(targetValue = 2000f))
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(388.dp)
                    .constrainAs(spotImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

            )
            RoundedLightGoToSpotButton(
                onClick = {
                    navigateTo(
                        "spot_detail_screen/spotReference=${
                            state.postInfo.spotRef.substringAfter(
                                "/"
                            )
                        }"
                    )
                },
                modifier = Modifier.constrainAs(spotIconButton) {
                    top.linkTo(parent.top, 16.dp)
                    start.linkTo(parent.start, 24.dp)
                })
            RoundedLightSaveButton(onClick = {}, modifier = Modifier.constrainAs(saveIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(parent.end, 24.dp)
            }, isSaved = false)
            RoundedLightSendButton(modifier = Modifier.constrainAs(sendIconButton) {
                top.linkTo(parent.top, 16.dp)
                end.linkTo(saveIconButton.start, 16.dp)
            }, onClick = {
                context.startActivity(
                    getShareIntent(
                        shareText = shareText,
                        null
                    )
                )
            })
            RoundedLightLikeButton(onClick = {
                postLikeClick()
            }, modifier = Modifier.constrainAs(likeIconButton) {
                end.linkTo(parent.end, 24.dp)
                bottom.linkTo(parent.bottom, 16.dp)
            }, isLiked = state.postIsLiked)
        }

        //Post author user information
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (spotterImage, postedByTitle, postedByUsername, createdDate) = createRefs()
            ProfileImage(
                imageUrl = state.postInfo.author.image,
                size = 56.dp,
                modifier = Modifier.constrainAs(spotterImage) {
                    start.linkTo(parent.start, 24.dp)
                    top.linkTo(parent.top, (-16).dp)
                })
            Text(
                text = stringResource(id = R.string.posted_by),
                style = secondaryRegularBodyS,
                modifier = Modifier.constrainAs(postedByTitle) {
                    top.linkTo(parent.top, 4.dp)
                    start.linkTo(spotterImage.end, 8.dp)
                })
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    "@${state.postInfo.author.username}"
                },
                style = primaryBoldHeadlineXS,
                modifier = Modifier
                    .constrainAs(postedByUsername) {
                        top.linkTo(postedByTitle.bottom)
                        start.linkTo(spotterImage.end, 8.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 100.dp))
            Text(
                text = if (showShimmer.value) {
                    ""
                } else {
                    stringResource(id = R.string.created) + " " + formatDate(state.postInfo.creation_date)
                },
                style = secondaryRegularBodyS,
                color = Color(0xFF333333),
                modifier = Modifier
                    .constrainAs(createdDate) {
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(parent.top, 4.dp)
                    }
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 100.dp)
            )
        }
        CustomSpacer(size = 24.dp)
        //Post description
        if (state.postInfo.description.isNotBlank()) {
            Text(
                text = state.postInfo.description,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis,
                style = secondaryRegularBodyM,
                modifier = Modifier
                    .background(shimmerBrush(showShimmer = showShimmer.value))
                    .defaultMinSize(minWidth = 400.dp)
                    .padding(horizontal = 24.dp)
            )
            CustomSpacer(size = 4.dp)
        }

        Text(
            text = "${state.postLikes}" + " " + stringResource(id = R.string.likes),
            style = secondarySemiBoldBodyM,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        CustomSpacer(size = 4.dp)
        Text(
            text = if (state.postInfo.comments.isNotEmpty()) {
                stringResource(id = R.string.view_all) + " ${state.postInfo.comments.size} " + stringResource(
                    id = R.string.comments
                )
            } else {
                stringResource(id = R.string.be_the_first_to_comment)
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clickable { navigateTo("comments_screen/postReference=${state.postInfo.id}") },
            color = Color(0xFF999999)
        )

    }
}