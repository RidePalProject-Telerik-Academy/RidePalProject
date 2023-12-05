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
