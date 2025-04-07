// JavaScript to control modal visibility
//function openModal(modalId) {
//    document.getElementById(modalId).classList.remove('hidden');
//}
//
//function closeModal(modalId) {
//    document.getElementById(modalId).classList.add('hidden');
//}

//-----------------------collect all tabs-------------------
const tabs = document.getElementsByClassName("tabs");
//--------------collect all respective contents-----------------
const content = document.getElementsByClassName("content");

function showContent(index){
    for(let i=0; i<content.length; i++){
        content[i].classList.add('hidden');
    }
    content[index].classList.remove('hidden');
}
function HighLightTab(index){
    for(let i=0; i<tabs.length; i++){
        tabs[i].classList.remove('highlight');
    }
    tabs[index].classList.add('highlight');

}

//--------------event listener for tab click-----------------
for(let i=0; i<tabs.length; i++){
    tabs[i].addEventListener("click", function(){
        showContent(i);
        HighLightTab(i);
    });

}
//default content and tab
    tabs[0].classList.add('highlight');
    content[0].classList.remove('hidden');

//---------------------------------responsive side bar-------------------------------
//bar = document.getElementById('bar');
////-------------------set bar == false-----------------
//bar.checked=false;
//
//tabContents = document.getElementsByTagName("span");
//const heading = document.querySelector(".side-bar h1");
//bar.addEventListener("click", function(){
//    if (bar.checked==true) {
//        console.log("checked");
//        for(let i=0; i<tabContents.length;i++){
//            tabContents[i].style.display = 'block';
//        }
//            heading.style.display = 'block';
//
//    }
//    else{
//        console.log("unchecked");
//         heading.style.display = 'none';
//
//        for(let i=0; i<tabContents.length;i++){
//            tabContents[i].style.display = 'none';
//        }
//    }
//
//})
