
let options = document.querySelectorAll('.dropdown-item')

for (o of options){
	o.addEventListener("click", function(ev){
		if(ev.target.classList.contains("active")){
			return;
		}
		else{
			activeOptions = document.querySelectorAll('.active');
			for(a of activeOptions){
				a.classList.remove("active");
			}
			ev.target.classList.add("active");
		}
		
	})
}
