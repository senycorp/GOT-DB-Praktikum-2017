<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${animal.name}</h2>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Herkunftsort</h3>
                    <hr />
                    <p>${animal.heimat} <a href="/location/${animal.heimatId}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></p>
                </div>
                <div class="col-lg-12">
                    <h3>Besitzer</h3>
                    <hr />
                    <p>${animal.besitzerName} <a href="/person/${animal.besitzerId}" class="btn btn-primary">Öffnen <i class="fa fa-arrow-circle-o-right"></i></a></p>
                </div>
            </div>
        </div>
    </div>
</section>