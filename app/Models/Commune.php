<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Commune extends Model
{
    //
    protected $fillable = [
        'nom',
        'district_id',
    ];
}
