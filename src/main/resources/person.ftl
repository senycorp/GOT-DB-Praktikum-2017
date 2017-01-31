<section id="person">
    <div class="container">
        <div class="row">
            <div class="col-lg-12 text-center">
                <h2>${person.name}</h2>
                <h3>${person.titel}</h3>
                <hr class="star-primary">
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <div class="col-lg-12">
                    <h3>Biographie</h3>
                    <hr />
                    <p>${person.biografie}</p>
                </div>
                <div class="col-lg-12">
                    <h3>Herkunftsort</h3>
                    <hr />
                    <p>${person.heimat}</p>
                </div>
                <div class="col-lg-12">
                    <h3>Häuser</h3>
                    <hr />
                    <#list person.haeuser as haus>
                        <div class="label label-info"><i class="fa fa-calendar"></i> ${haus.von} - ${haus.bis}</div> - ${haus.name} <br/>
                    </#list>
                </div>
                <div class="col-lg-12">
                    <h3>Beziehungen</h3>
                    <hr />
                    <#list person.beziehungen as beziehung>
                        ${beziehung.name} - ${beziehung.beziehungsArt}<br />
                    </#list>
                </div>
                <div class="col-lg-12">
                    <h3>Tiere</h3>
                    <hr />
                <#list person.tiere as animal>
                ${animal.name}
                </#list>
                </div>
            </div>
        </div>
    </div>
</section>