<?php

namespace App\Models;

use App\Models\Client;
use App\Models\Commune;
use Illuminate\Database\Eloquent\Model;

class AddressClient extends Model
{
    //
    protected $fillable = [
        'commune_id',
        'client_id',
        'avenue',
        'quartier',
        'numero_parcelle'
    ];

    public function client(){
        return $this->belongsTo(Client::class, 'client_id' , 'id');
    }

    public function commune(){
        return $this->belongsTo(Commune::class, 'commune_id' , 'id');
    }
}
