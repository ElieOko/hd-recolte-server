<?php

namespace App\Models;

use App\Models\ClientTrash;
use App\Models\AddressClient;
use Illuminate\Database\Eloquent\Model;

class Client extends Model
{
    //
    protected $fillable = [
        'code',
        'nom',
        'prenom',
        'telephone'
    ];

    public function address_client(){
        return $this->hasMany(AddressClient::class);
    }
    public function trash_client(){
        return $this->hasMany(ClientTrash::class);
    }
}
