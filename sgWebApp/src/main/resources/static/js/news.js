function docFill() {
    if ($(document).height() <= $(window).height()) {
        $(".docFill").css(
                "height",
                $(window).height() - $("body").height()
                        + $(".docFill").height());
    } else {
        $(".docFill").css("height", "auto");
    }
};
$(document).ready(docFill);
$(window).resize(docFill);