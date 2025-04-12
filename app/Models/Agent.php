<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Agent extends Model
{
    //
    protected $fillable = [
        "code",
        "nom",
        "prenom",
        "postnom",
        "date_naissance",
        "genre",
        "telephone",
        "address"
    ];
}
