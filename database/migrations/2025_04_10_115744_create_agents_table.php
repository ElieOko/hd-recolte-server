<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('agents', function (Blueprint $table) {
            $table->id();
            $table->text("code")->nullable();
            $table->integer("city_id")->default(1);
            $table->text("nom");
            $table->text("postnom");
            $table->text("prenom");
            $table->text("telephone")->nullable();
            $table->text("genre")->nullable();
            $table->text("address")->nullable();
            $table->text("date_naissance")->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('agents');
    }
};
