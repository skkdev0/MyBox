const pianoKeys = document.querySelectorAll(".piano-keys .key"),
volumeSlider = document.querySelector(".volume-slider input"),
keysCheckbox = document.querySelector(".keys-checkbox input");

let allKeys = [],
audio = new Audio(`tunes/a.wav`); 

const playTune = (key) => {
    audio.src = `tunes/${key}.wav`; 
    audio.play(); 

    const clickedKey = document.querySelector(`[data-key="${key}"]`);
    if(clickedKey) { // Check if key exists
        clickedKey.classList.add("active");
        setTimeout(() => {
            clickedKey.classList.remove("active");
        }, 150);
    }
}

pianoKeys.forEach(key => {
    allKeys.push(key.dataset.key);
    key.addEventListener("click", () => playTune(key.dataset.key));
});

const handleVolume = (e) => {
    audio.volume = e.target.value;
}

const showHideKeys = () => {
    pianoKeys.forEach(key => {
        // Toggle 'hide' class on the list item (li)
        key.classList.toggle("hide");
    });
}

// Checkbox click ki jagah 'change' use karein
keysCheckbox.addEventListener("change", showHideKeys);
volumeSlider.addEventListener("input", handleVolume);
document.addEventListener("keydown", (e) => {
    if(allKeys.includes(e.key)) playTune(e.key);
});
