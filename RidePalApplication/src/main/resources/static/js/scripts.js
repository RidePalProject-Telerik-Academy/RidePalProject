$(document).ready(function () {
    var durationSlider = $("#durationSlider").ionRangeSlider({
        type: "double",
        grid: true,
        min: 0,
        max: 600,
        step: 15,
        from:  0,
        to:  600,
        onChange: function (data) {
            $("#minDuration").val(data.from * 60);
            $("#maxDuration").val(data.to * 60);
        }
    }).data("ionRangeSlider");
});
function convertAndSubmit() {
    $("#filterForm").submit();
}

    function incrementPage() {
    currentPage++;

    window.location.href = '/playlists?page=' + currentPage;
}

$(document).ready(function () {
    var durationSlider = $("#durationSlider").ionRangeSlider({
        type: "double",
        grid: true,
        min: 0,
        max: 600,
        step: 15,
        from:  0,
        to:  600,
        onChange: function (data) {
            $("#minDuration").val(data.from * 60);
            $("#maxDuration").val(data.to * 60);
        }
    }).data("ionRangeSlider");
});
function convertAndSubmit() {
    $("#filterForm").submit();
}

function incrementPage() {
    currentPage++;

    window.location.href = '/playlists?page=' + currentPage;
}

$(document).ready(function() {
    // Make an AJAX request to Pixabay API
    $.ajax({
        url: 'https://pixabay.com/api/',
        data: {
            key: '41082399-0cbfbaf732e4956f03901ab47',
            q: 'music',
            order: 'popular'
        },
        success: function(response) {
            // Update the playlist image source with a random image from Pixabay
            var randomImage = response.hits[Math.floor(Math.random() * response.hits.length)];
            $('#singlePlaylistImage').attr('src', randomImage.webformatURL);
        }
    });
});

    $(document).ready(function () {
    // Make an AJAX request to Pixabay API
    $.ajax({
        url: 'https://pixabay.com/api/',
        data: {
            key: '41082399-0cbfbaf732e4956f03901ab47',
            q: 'music',
            order: 'popular',
        },
        success: function (response) {
            $('.playlistImage').each(function () {
                var randomImage = response.hits[Math.floor(Math.random() * response.hits.length)];
                $(this).attr('src', randomImage.webformatURL);
            });
        }
    });
});

$(document).ready(function () {
    // Make an AJAX request to Pixabay API
    $.ajax({
        url: 'https://pixabay.com/api/',
        data: {
            key: '41082399-0cbfbaf732e4956f03901ab47',
            q: 'happy people',
            order: 'popular'
        },
        success: function (response) {
            $('.profileImage').each(function () {
                var randomImage = response.hits[Math.floor(Math.random() * response.hits.length)];
                $(this).attr('src', randomImage.webformatURL);
            });
        }
    });
});
