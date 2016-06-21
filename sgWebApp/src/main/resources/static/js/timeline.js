$(document).ready(function() {
    //
    reset();
    
    //
    $('#scroller').jscroll({
        debug: true,
        nextSelector: 'a.jscroll-next:last',
        callback: reset
    });    
    
});

function reset() {
    //
    console.log("reset()");
    
    //
    $('.thumbnail').hover(function() {
        $(this).find('.caption').slideDown(250); //.fadeIn(250)
    }, function() {
        $(this).find('.caption').slideUp(250); //.fadeOut(205)
    });
};
