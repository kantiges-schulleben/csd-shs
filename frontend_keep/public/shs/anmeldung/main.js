const filterList = (searchTerm, target) => {
  var options = target.parentElement.parentElement.childNodes;
  options.forEach(optGroup => {
    if (optGroup.className == "options-container active") {
      options = optGroup;
    }
  });
  searchTerm = searchTerm.toLowerCase();
  console.log(options);

  options.childNodes.forEach(option => {
    if (option.tagName == "DIV") {
      let label = option.firstElementChild.nextElementSibling.innerText.toLowerCase();
      if (label.indexOf(searchTerm) != -1) {
        option.style.display = "block";
      } else {
        option.style.display = "none";
      }
    }
  });
};

const selected = document.querySelectorAll(".selected");
const optionsContainer = document.querySelector(".options-container");
const searchBox = document.querySelectorAll(".search-box input");

const optionsList = document.querySelectorAll(".option");

selected.forEach(sel => {
  sel.addEventListener("click", () => {
    event.target.parentElement.childNodes.forEach(childNode => {
      if (childNode.className == "options-container") {
        childNode.classList.toggle("active");
        if (childNode.classList.contains("active")) {
          event.target.parentElement.childNodes.forEach(childNode2 => {
            if (childNode2.className == "search-box") {
              childNode2.focus();
            }
          });
        }
      } else if (childNode.className == "search-box") {
        childNode.value = "";
        // sel.
        childNode.childNodes.forEach(maybeInput => {
          if (maybeInput.tagName == "INPUT") {
            filterList("", maybeInput);
          }
        });
      }
    });
  });
});

optionsList.forEach(o => {
  o.addEventListener("click", () => {
    event.target.parentElement.classList.remove("active");

    event.target.parentElement.parentElement.childNodes.forEach(element => {
      if (element.className == "selected") {
        element.innerHTML = o.querySelector("label").innerHTML;
      }
    });
  });
});

searchBox.forEach(box => {
  box.addEventListener("keyup", function (e) {
    filterList(e.target.value, box);
  });
});