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


function incrementPage(pageSize) {
    if(currentPage++ < pageSize) {
        window.location.href = '/playlists?page=' + currentPage;
    }
}

function decrementPage() {
    if(currentPage > 0) {
        currentPage--;
        window.location.href = '/playlists?page=' + currentPage;
    }
}
function incrementSongPage(pageSize) {
    if(currentPage++ < pageSize) {
        window.location.href = '/songs?page=' + currentPage;
    }
}

function decrementSongPage() {
    if(currentPage > 0) {
        currentPage--;
        window.location.href = '/songs?page=' + currentPage;
    }
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
    function setupDeleteConfirmation(selector) {
        var elements = document.querySelectorAll(selector);
        for (var i = 0; i < elements.length; i++) {
            elements[i].addEventListener('click', function (event) {
                var choice = confirm(this.getAttribute('data-confirm'));

                if (!choice) {
                    event.preventDefault();
                }
                // If choice is true, allow the default action (navigation or form submission)
            });
        }
    }

    document.addEventListener('DOMContentLoaded', function () {
        setupDeleteConfirmation('.delete-user');
        setupDeleteConfirmation('.delete-element-btn');
        setupDeleteConfirmation('.delete-element-utility-btn');
    });
    setupDeleteConfirmation('.delete-user');
    setupDeleteConfirmation('.delete-element-btn');
});

