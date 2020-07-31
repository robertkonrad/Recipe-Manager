    function addItem(){
        var input = document.createElement("input");
        input.type = "text";
        input.name = "ingredient-li";
        input.placeholder = "Ingredient";
        var input1 = document.createElement("input");
        input1.type = "number";
        input1.name = "amount-li";
        input1.placeholder = "Amount";
        input1.value = "0";
        input1.step = "0.01";
        var input2 = document.createElement("input");
        input2.type = "text";
        input2.name = "unit-li";
        input2.placeholder = "Unit";
        var li = document.createElement("li");
        li.appendChild(input);
        li.appendChild(input1);
        li.appendChild(input2);
        var ul = document.getElementById("ingredients-ul");
        var removeBtn = document.createElement("input");
        removeBtn.type = "button";
        removeBtn.value = "-";
        removeBtn.setAttribute("onclick", "remove(this)");
        li.appendChild(removeBtn);
        ul.appendChild(li);
    }

    function remove(e){
        var element = e;
        element.parentNode.remove();
    }

    function addSearchItem(){
        var input = document.createElement("input");
        input.type = "text";
        input.name = "ingredient-li";
        input.placeholder = "Ingredient";
        var li = document.createElement("li");
        li.appendChild(input);
        var ul = document.getElementById("ingredients-ul");
        var removeBtn = document.createElement("input");
        removeBtn.type = "button";
        removeBtn.value = "-";
        removeBtn.setAttribute("onclick", "remove(this)");
        li.appendChild(removeBtn);
        ul.appendChild(li);
    }